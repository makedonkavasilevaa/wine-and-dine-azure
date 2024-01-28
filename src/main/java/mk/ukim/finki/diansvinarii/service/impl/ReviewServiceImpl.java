package mk.ukim.finki.diansvinarii.service.impl;

import mk.ukim.finki.diansvinarii.model.Review;
import mk.ukim.finki.diansvinarii.model.User;
import mk.ukim.finki.diansvinarii.model.Vinarija;
import mk.ukim.finki.diansvinarii.repository.ReviewRepository;
import mk.ukim.finki.diansvinarii.service.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final VinarijaServiceImpl vinarijaService;
    private final UserServiceImpl userService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, VinarijaServiceImpl vinarijaService, UserServiceImpl userService) {
        this.reviewRepository = reviewRepository;
        this.vinarijaService = vinarijaService;
        this.userService = userService;
    }

    public List<Review> findAllByWinery_Id(Long id){
        return reviewRepository.findAllByWinery_Id(id);
    }

    public Double getWineryAverageScoreById(Long id){
        List<Review> reviews = reviewRepository.findAllByWinery_Id(id);
        return reviews.stream().map(Review::getScore).mapToInt(Integer::intValue).average().orElse(0);
    }

    public List<Review> getNMostRecentByWineryId(Long id, int n){
        return reviewRepository.findAllByWinery_IdOrderByTimestampDesc(id).stream().limit(n).toList();
    }
    public List<Review> getNBestByWineryId(Long id, int n){
        return reviewRepository.findAllByWinery_IdOrderByScore(id).stream().limit(n).toList();
    }

    public List<Review> findAllByWinery_IdOrderByScore(Long id){
        return reviewRepository.findAllByWinery_IdOrderByScore(id);
    }
    public List<Review> findAllByWinery_IdOrderByTimestampDesc(Long id){
        return reviewRepository.findAllByWinery_IdOrderByTimestampDesc(id);
    }

    public Review findById(Long id){
        return reviewRepository.findById(id).orElse(null);
    }

    public Review add(Review review){
        return reviewRepository.save(review);
    }

    public Review create(Long id, int score, String desc, LocalDateTime timestamp, Long userId) {
        Vinarija winery = null;
        User createdBy = null;
        try {
            winery = vinarijaService.findById(id).orElse(null);
            createdBy = userService.findById(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Review review = new Review(score, desc, winery, timestamp, createdBy);
        return reviewRepository.save(review);
    }

    public void delete(Review review){
        reviewRepository.delete(review);
    }


}
