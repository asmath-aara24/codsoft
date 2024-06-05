java code:
import java.util.*;

class User {
    String name;
    Map<String, Integer> ratings;  // Movie -> Rating

    public User(String name) {
        this.name = name;
        this.ratings = new HashMap<>();
    }

    public void rateMovie(String movie, int rating) {
        ratings.put(movie, rating);
    }

    public Map<String, Integer> getRatings() {
        return ratings;
    }

    public String getName() {
        return name;
    }
}

class Movie {
    String title;

    public Movie(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

class RecommendationSystem {
    private List<User> users;

    public RecommendationSystem() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<String> recommendMovies(User user) {
        Map<User, Double> similarityScores = new HashMap<>();

        for (User otherUser : users) {
            if (!otherUser.equals(user)) {
                double similarity = calculateSimilarity(user, otherUser);
                similarityScores.put(otherUser, similarity);
            }
        }

        List<User> similarUsers = new ArrayList<>(similarityScores.keySet());
        similarUsers.sort((u1, u2) -> Double.compare(similarityScores.get(u2), similarityScores.get(u1)));

        Set<String> recommendedMovies = new HashSet<>();
        for (User similarUser : similarUsers) {
            for (Map.Entry<String, Integer> entry : similarUser.getRatings().entrySet()) {
                if (!user.getRatings().containsKey(entry.getKey()) && entry.getValue() >= 4) {
                    recommendedMovies.add(entry.getKey());
                }
            }
        }

        return new ArrayList<>(recommendedMovies);
    }

    private double calculateSimilarity(User user1, User user2) {
        Map<String, Integer> ratings1 = user1.getRatings();
        Map<String, Integer> ratings2 = user2.getRatings();

        Set<String> commonMovies = new HashSet<>(ratings1.keySet());
        commonMovies.retainAll(ratings2.keySet());

        if (commonMovies.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for (String movie : commonMovies) {
            sum += ratings1.get(movie) * ratings2.get(movie);
        }

        double magnitude1 = Math.sqrt(ratings1.values().stream().mapToInt(i -> i * i).sum());
        double magnitude2 = Math.sqrt(ratings2.values().stream().mapToInt(i -> i * i).sum());

        return sum / (magnitude1 * magnitude2);
    }
}

public class Main {
    public static void main(String[] args) {
        User user1 = new User("Alice");
        user1.rateMovie("Inception", 5);
        user1.rateMovie("Titanic", 3);
        user1.rateMovie("Avatar", 4);

        User user2 = new User("Bob");
        user2.rateMovie("Inception", 4);
        user2.rateMovie("Titanic", 5);
        user2.rateMovie("Avatar", 2);

        User user3 = new User("Charlie");
        user3.rateMovie("Inception", 5);
        user3.rateMovie("Titanic", 4);
        user3.rateMovie("Avatar", 4);
        user3.rateMovie("Interstellar", 5);

        RecommendationSystem system = new RecommendationSystem();
        system.addUser(user1);
        system.addUser(user2);
        system.addUser(user3);

        List<String> recommendations = system.recommendMovies(user1);
        System.out.println("Recommended movies for " + user1.getName() + ": " + recommendations);
    }
}