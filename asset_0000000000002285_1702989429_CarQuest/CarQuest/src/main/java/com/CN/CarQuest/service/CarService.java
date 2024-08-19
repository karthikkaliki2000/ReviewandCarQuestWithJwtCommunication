package com.CN.CarQuest.service;

import com.CN.CarQuest.communicator.ReviewServiceCommunicator;
import com.CN.CarQuest.dto.CarRequest;
import com.CN.CarQuest.dto.CarResponse;
import com.CN.CarQuest.dto.ReviewRequest;
import com.CN.CarQuest.dto.ReviewResponse;
import com.CN.CarQuest.model.Car;
import com.CN.CarQuest.model.Review;
import com.CN.CarQuest.repository.CarRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {

	private final CarRepository carRepository;
	

	private final ReviewServiceCommunicator reviewServiceCommunicator;

	public CarService(CarRepository carRepository, ReviewServiceCommunicator reviewServiceCommunicator) {
		this.carRepository = carRepository;
		this.reviewServiceCommunicator=reviewServiceCommunicator;
	}

	
	

  

	public List<Car> getAllCars() {
		return carRepository.findAll();
	}

	public Car updateCar(String name, CarRequest carDto) {
		Car existingCar = carRepository.findById(name).orElseThrow(() ->
				new RuntimeException("Car not found with id: " + name));
		existingCar.setName(carDto.getName());
		existingCar.setBrand(carDto.getBrand());
		existingCar.setPrice(carDto.getPrice());
		existingCar.setColor(carDto.getColor());
		existingCar.setModelYear(carDto.getModelYear());
		return carRepository.save(existingCar);
	}

	public void deleteCar(String name) {
		carRepository.deleteById(name);
	}

	public Car addCar(CarRequest carDto) {
		Car car = new Car();
		car.setName(carDto.getName());
		car.setBrand(carDto.getBrand());
		car.setPrice(carDto.getPrice());
		car.setColor(carDto.getColor());
		car.setModelYear(carDto.getModelYear());

		return carRepository.save(car);
	}

	public void addReview(ReviewRequest reviewRequest, String authorizationHeader) {
		String token=authorizationHeader.replace("Bearer ", "");
		reviewServiceCommunicator.addReview(reviewRequest, token);
	}
	
	

	  public CarResponse getCarById(String name, String authorizationHeader) {
			String token=authorizationHeader.replace("Bearer ", "");
			Car car = carRepository.findById(name).orElseThrow(() ->
					new RuntimeException("Car not found with id: " + name));
			List<ReviewResponse> reviewsResponses=reviewServiceCommunicator.getReview(name, token);
			List<String> reviews=new ArrayList<String>();
			for(ReviewResponse r:reviewsResponses) {
			//	reviews.add(r.getName());
				reviews.add(r.getReview());
			}
			return CarResponse.builder().brand(car.getBrand()).color(car.getColor())
					.price(car.getPrice()).modelYear(car.getModelYear()).name(car.getName()).reviews(reviews).build();
	    }
}
