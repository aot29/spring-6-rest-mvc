package guru.springframework.spring6RestMvc.services;

import guru.springframework.spring6RestMvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();

    Beer getBeerById(UUID id);
}
