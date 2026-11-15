import java.io.*;
import java.util.*;
/** Railway Reservation System (PS 66) - Graph, Queue, HashMap, Merge Sort, File Handling */
public class RailwayReservationSystem {
    static class Edge {
        String to; int distance;
        Edge(String to, int distance) { this.to = to; this.distance = distance; }
    }
    /** Confirmed ticket (TKTxxx) or waiting-list entry (WLx). */
    static class Ticket {
        String id, passenger, trainId;
        Ticket(String id, String passenger, String trainId) { this.id = id; this.passenger = passenger; this.trainId = trainId; }
    }
    static class Train {
        String id, name, source, destination, departure; // departure = HH:mm
        int fare, capacity, booked = 0;
        Queue<Ticket> waiting = new LinkedList<>();       // FIFO waiting list
        Train(String id, String name, String source, String destination, int fare, String departure, int capacity) {
            this.id = id; this.name = name; this.source = source; this.destination = destination;
            this.fare = fare; this.departure = departure; this.capacity = capacity;
        }
        int departureMinutes() {
            String[] p = departure.split(":");
            return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
        }
    }
    // ===================== GRAPH (stations & routes) =====================
    static class Graph {
        Map<String, List<Edge>> adj = new LinkedHashMap<>();
        void addStation(String s) { adj.putIfAbsent(s, new ArrayList<>()); }
        void addRoute(String a, String b, int dist) {
            addStation(a); addStation(b);
            adj.get(a).removeIf(e -> e.to.equals(b));   // avoid duplicate edges
            adj.get(b).removeIf(e -> e.to.equals(a));
            adj.get(a).add(new Edge(b, dist));
            adj.get(b).add(new Edge(a, dist));          // undirected
        }
        /** Returns the stored spelling of a station (case-insensitive), or null. */
        String resolve(String s) {
            if (s == null) return null;
            for (String k : adj.keySet()) if (k.equalsIgnoreCase(s.trim())) return k;
            return null;
        }
        /** Dijkstra: shortest route by distance. Returns path (or null). */
        List<String> shortestRoute(String src, String dst, int[] totalOut) {
            src = resolve(src); dst = resolve(dst);
            if (src == null || dst == null) return null;
            Map<String, Integer> dist = new HashMap<>();
            Map<String, String> prev = new HashMap<>();
            for (String s : adj.keySet()) dist.put(s, Integer.MAX_VALUE);
            dist.put(src, 0);
            PriorityQueue<Object[]> pq = new PriorityQueue<>(Comparator.comparingInt(o -> (int) o[1]));
            pq.add(new Object[]{src, 0});
            while (!pq.isEmpty()) {
                Object[] cur = pq.poll();
                String u = (String) cur[0];
                int d = (int) cur[1];
                if (d > dist.get(u)) continue;
                for (Edge e : adj.get(u)) {
                    int nd = d + e.distance;
                    if (nd < dist.get(e.to)) { dist.put(e.to, nd); prev.put(e.to, u); pq.add(new Object[]{e.to, nd}); }
                }
            }
            if (dist.get(dst) == Integer.MAX_VALUE) return null;
            LinkedList<String> path = new LinkedList<>();
            for (String at = dst; at != null; at = prev.get(at)) path.addFirst(at);
            totalOut[0] = dist.get(dst);
            return path;
        }
        void display() {
            if (adj.isEmpty()) { System.out.println("No stations."); return; }
            for (Map.Entry<String, List<Edge>> en : adj.entrySet()) {
                StringBuilder sb = new StringBuilder(en.getKey() + " -> ");
                for (Edge e : en.getValue()) sb.append(e.to).append("(").append(e.distance).append("km) ");
                System.out.println(sb);
            }
        }
    }
    // ===================== SORTING (Merge Sort) =====================
    static <T> List<T> mergeSort(List<T> list, Comparator<T> cmp) {
        if (list.size() <= 1) return list;
        int mid = list.size() / 2;
        List<T> left = mergeSort(new ArrayList<>(list.subList(0, mid)), cmp);
        List<T> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())), cmp);
        List<T> res = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size())
            res.add(cmp.compare(left.get(i), right.get(j)) <= 0 ? left.get(i++) : right.get(j++));
        while (i < left.size()) res.add(left.get(i++));
        while (j < right.size()) res.add(right.get(j++));
        return res;
    }
    // ===================== RESERVATION SYSTEM =====================
    static class ReservationSystem {
        Graph graph = new Graph();
        Map<String, Train> trains = new LinkedHashMap<>();   // trainId -> Train
        Map<String, Ticket> tickets = new HashMap<>();       // ticketId -> Ticket (hashing)
        int ticketCounter = 101, waitCounter = 1;
        void viewTrains(Collection<Train> list) {
            if (list.isEmpty()) { System.out.println("No trains available."); return; }
            System.out.printf("%-6s %-18s %-12s %-12s %-6s %-6s %-18s %s%n",
                    "ID", "Name", "From", "To", "Fare", "Dep", "Seats(free/total)", "Waiting");
            for (Train t : list)
                System.out.printf("%-6s %-18s %-12s %-12s %-6d %-6s %-18s %d%n", t.id, t.name, t.source,
                        t.destination, t.fare, t.departure, (t.capacity - t.booked) + "/" + t.capacity, t.waiting.size());
        }
        String searchRoute(String src, String dst) {
            int[] d = new int[1];
            List<String> path = graph.shortestRoute(src, dst, d);
            if (path == null) return "No route found between " + src + " and " + dst + ".";
            String rs = graph.resolve(src), rd = graph.resolve(dst);
            StringBuilder sb = new StringBuilder("Shortest route: " + String.join(" -> ", path) + " (" + d[0] + " km)\n");
            List<String> direct = new ArrayList<>();
            for (Train t : trains.values())
                if (t.source.equalsIgnoreCase(rs) && t.destination.equalsIgnoreCase(rd)) direct.add(t.id);
            return sb.append(direct.isEmpty() ? "No direct train between these stations."
                                              : "Direct trains: " + String.join(" ", direct)).toString();
        }
        private boolean alreadyBooked(Train t, String passenger) {
            for (Ticket tk : tickets.values())
                if (tk.trainId.equals(t.id) && tk.passenger.equalsIgnoreCase(passenger)) return true;
            for (Ticket w : t.waiting) if (w.passenger.equalsIgnoreCase(passenger)) return true;
            return false;
        }
        Ticket findConfirmedByPassenger(String passenger) {
            for (Ticket tk : tickets.values()) if (tk.passenger.equalsIgnoreCase(passenger)) return tk;
            return null;
        }
        String book(String trainId, String passenger) {
            Train t = trains.get(trainId.trim().toUpperCase());
            if (t == null) return "Train " + trainId + " not found.";
            passenger = passenger.replace("|", "").trim();
            if (passenger.isEmpty()) return "Invalid passenger name.";
            if (alreadyBooked(t, passenger))
                return passenger + " already has a booking/waiting entry on train " + t.id + ".";
            if (t.booked < t.capacity) {
                Ticket tk = new Ticket("TKT" + (ticketCounter++), passenger, t.id);
                tickets.put(tk.id, tk);
                t.booked++;
                return "Ticket booked: " + tk.id + " | " + passenger + " | Train " + t.id;
            }
            Ticket w = new Ticket("WL" + (waitCounter++), passenger, t.id);
            t.waiting.add(w);                                          // enqueue
            return "Train full. Added to waiting queue: " + w.id + " | position " + t.waiting.size();
        }
        String searchTicket(String id) {
            id = id.trim().toUpperCase();
            Ticket tk = tickets.get(id);                               // O(1) hash lookup
            if (tk != null) return "Ticket found: " + tk.id + " | " + tk.passenger + " | Train " + tk.trainId + " | CONFIRMED";
            for (Train t : trains.values()) {
                int pos = 1;
                for (Ticket w : t.waiting) {
                    if (w.id.equals(id))
                        return "Waiting entry: " + w.id + " | " + w.passenger + " | Train " + t.id + " | position " + pos;
                    pos++;
                }
            }
            return "Ticket " + id + " not found.";
        }
        String cancel(String id) {
            id = id.trim().toUpperCase();
            if (id.startsWith("WL")) {                                 // cancel a waiting entry
                for (Train t : trains.values()) {
                    Iterator<Ticket> it = t.waiting.iterator();
                    while (it.hasNext())
                        if (it.next().id.equals(id)) { it.remove(); return "Waiting-list entry " + id + " cancelled."; }
                }
                return "Ticket " + id + " not found.";
            }
            Ticket tk = tickets.remove(id);                            // cancel a confirmed ticket
            if (tk == null) return "Ticket " + id + " not found.";
            Train t = trains.get(tk.trainId);
            if (t == null) return "Ticket cancelled (train record missing).";
            t.booked--;
            if (t.waiting.isEmpty()) return "Ticket cancelled.";
            Ticket next = t.waiting.poll();                            // dequeue (FIFO)
            Ticket nt = new Ticket("TKT" + (ticketCounter++), next.passenger, t.id);
            tickets.put(nt.id, nt);
            t.booked++;
            return "Ticket cancelled. Seat allocated to " + next.passenger + " from waiting queue ("
                    + next.id + "). New ticket: " + nt.id;
        }
        List<Train> sortByFare() {
            return mergeSort(new ArrayList<>(trains.values()), Comparator.comparingInt(t -> t.fare));
        }
        List<Train> sortByDeparture() {
            return mergeSort(new ArrayList<>(trains.values()), Comparator.comparingInt(Train::departureMinutes));
        }
        // ---------- File Handling ----------
        void load(String file) throws IOException {
            File f = new File(file);
            if (f.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) continue;
                        String[] p = line.split("\\|");
                        try {
                            switch (p[0]) {
                                case "STATION": graph.addStation(p[1]); break;
                                case "ROUTE": graph.addRoute(p[1], p[2], Integer.parseInt(p[3])); break;
                                case "TRAIN": {
                                    Train nt = new Train(p[1].toUpperCase(), p[2], p[3], p[4],
                                            Integer.parseInt(p[5]), p[6], Integer.parseInt(p[7]));
                                    nt.departureMinutes();                // validates HH:mm format
                                    trains.put(nt.id, nt);
                                    break;
                                }
                                case "TICKET": {
                                    Train t = trains.get(p[3].toUpperCase());
                                    if (t == null) { System.out.println("Skipping ticket for unknown train: " + line); break; }
                                    tickets.put(p[1].toUpperCase(), new Ticket(p[1].toUpperCase(), p[2], t.id));
                                    t.booked++;
                                    break;
                                }
                                case "WAIT": {
                                    Train t = trains.get(p[1].toUpperCase());
                                    if (t == null) break;
                                    if (p.length >= 4) t.waiting.add(new Ticket(p[2].toUpperCase(), p[3], t.id));
                                    else t.waiting.add(new Ticket("WL" + (waitCounter++), p[2], t.id)); // old format
                                    break;
                                }
                                case "COUNTER": ticketCounter = Integer.parseInt(p[1]); break;
                                case "WCOUNTER": waitCounter = Integer.parseInt(p[1]); break;
                            }
                        } catch (Exception ex) { System.out.println("Skipping bad line: " + line); }
                    }
                }
            }
            loadDefaults();   // fills in only what is missing (file absent, empty or corrupt)
        }
        void save(String file) throws IOException {
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.println("# Railway data file");
                for (String s : graph.adj.keySet()) pw.println("STATION|" + s);
                Set<String> done = new HashSet<>();
                for (Map.Entry<String, List<Edge>> en : graph.adj.entrySet())
                    for (Edge e : en.getValue()) {
                        String key = en.getKey().compareTo(e.to) < 0 ? en.getKey() + "#" + e.to : e.to + "#" + en.getKey();
                        if (done.add(key)) pw.println("ROUTE|" + en.getKey() + "|" + e.to + "|" + e.distance);
                    }
                for (Train t : trains.values())
                    pw.println("TRAIN|" + t.id + "|" + t.name + "|" + t.source + "|" + t.destination
                            + "|" + t.fare + "|" + t.departure + "|" + t.capacity);
                for (Ticket tk : tickets.values()) pw.println("TICKET|" + tk.id + "|" + tk.passenger + "|" + tk.trainId);
                for (Train t : trains.values())
                    for (Ticket w : t.waiting) pw.println("WAIT|" + t.id + "|" + w.id + "|" + w.passenger);
                pw.println("COUNTER|" + ticketCounter);
                pw.println("WCOUNTER|" + waitCounter);
            }
        }
        /** Adds default stations/trains only if the graph or train list is empty. */
        void loadDefaults() {
            if (graph.adj.isEmpty()) {
                graph.addRoute("Delhi", "Agra", 233);
                graph.addRoute("Agra", "Bhopal", 570);
                graph.addRoute("Delhi", "Jaipur", 280);
                graph.addRoute("Jaipur", "Mumbai", 1150);
                graph.addRoute("Bhopal", "Mumbai", 780);
            }
            if (trains.isEmpty()) {
                trains.put("T101", new Train("T101", "Rajdhani Exp", "Delhi", "Mumbai", 1800, "16:30", 2));
                trains.put("T102", new Train("T102", "Shatabdi Exp", "Delhi", "Agra", 700, "06:15", 3));
                trains.put("T103", new Train("T103", "Duronto Exp", "Delhi", "Bhopal", 1200, "22:00", 3));
            }
        }
    }
    // ===================== BUILT-IN TEST CASES =====================
    static void runTestCases() {
        System.out.println("\n===== Running 5 Test Cases (fresh temporary system) =====");
        ReservationSystem rs = new ReservationSystem();
        rs.loadDefaults();                       // T101 has capacity 2
        System.out.println("\nTest 1: Normal - Book one seat on T101 for Aman\n  " + rs.book("T101", "Aman"));
        rs.book("T101", "Rohan");                // fills the last seat
        System.out.println("\nTest 2: Boundary - T101 is full; Riya books\n  " + rs.book("T101", "Riya"));
        System.out.println("\nTest 3: Invalid - Search ticket TKT999\n  " + rs.searchTicket("TKT999"));
        System.out.println("\nTest 4: Cancel Aman's ticket while Riya is waitlisted\n  "
                + rs.cancel(rs.findConfirmedByPassenger("Aman").id));
        System.out.println("\nTest 5: Case-insensitive route search (delhi -> mumbai)\n  " + rs.searchRoute("delhi", "mumbai"));
        System.out.println("===== Test cases finished (nothing was saved) =====\n");
    }
    // ===================== MAIN =====================
    static final String DATA_FILE = "railway.txt";
    static final String[] MENU = {"1. View trains", "2. View stations & routes (graph)", "3. Search route",
            "4. Book ticket", "5. Search ticket", "6. Cancel ticket", "7. Sort trains by fare",
            "8. Sort trains by departure time", "9. View waiting list of a train", "10. Run test cases", "0. Save & Exit"};
    static void saveData(ReservationSystem rs) {
        try { rs.save(DATA_FILE); System.out.println("Data saved to " + DATA_FILE); }
        catch (IOException e) { System.out.println("Save failed: " + e.getMessage()); }
    }
    static String ask(Scanner sc, String prompt) { System.out.print(prompt); return sc.nextLine().trim(); }
    public static void main(String[] args) {
        ReservationSystem rs = new ReservationSystem();
        try {
            rs.load(DATA_FILE);
            System.out.println("Data loaded from " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Could not read file, using defaults: " + e.getMessage());
            rs = new ReservationSystem();
            rs.loadDefaults();
        }
        Scanner sc = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("\n===== RAILWAY RESERVATION SYSTEM =====");
                for (String m : MENU) System.out.println(m);
                switch (ask(sc, "Choice: ")) {
                    case "1": rs.viewTrains(rs.trains.values()); break;
                    case "2": rs.graph.display(); break;
                    case "3": System.out.println(rs.searchRoute(ask(sc, "From: "), ask(sc, "To: "))); break;
                    case "4": System.out.println(rs.book(ask(sc, "Train ID: "), ask(sc, "Passenger name: "))); break;
                    case "5": System.out.println(rs.searchTicket(ask(sc, "Ticket ID: "))); break;
                    case "6": System.out.println(rs.cancel(ask(sc, "Ticket ID: "))); break;
                    case "7": rs.viewTrains(rs.sortByFare()); break;
                    case "8": rs.viewTrains(rs.sortByDeparture()); break;
                    case "9": {
                        Train t = rs.trains.get(ask(sc, "Train ID: ").toUpperCase());
                        if (t == null) System.out.println("Train not found.");
                        else if (t.waiting.isEmpty()) System.out.println("Waiting (FIFO order): empty");
                        else {
                            System.out.println("Waiting (FIFO order):");
                            int pos = 1;
                            for (Ticket w : t.waiting) System.out.println("  " + (pos++) + ". " + w.id + " - " + w.passenger);
                        }
                        break;
                    }
                    case "10": runTestCases(); break;
                    case "0": saveData(rs); System.out.println("Thank you!"); return;
                    default: System.out.println("Invalid choice.");
                }
            }
        } catch (NoSuchElementException e) {   // Ctrl+D / end of input
            System.out.println("\nInput closed. Saving...");
            saveData(rs);
        }
    }
}