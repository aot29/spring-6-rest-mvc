package guru.springframework.spring6RestMvc.controller;

import guru.springframework.spring6RestMvc.model.Beer;
import guru.springframework.spring6RestMvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController

public class BeerController {
    public static final String BEER_PATH = "/api/v1/beer/";
    public static final String BEER_PATH_ID = BEER_PATH + "{beerId}";

    private final BeerService beerService;

    /**
     * Example:
     * curl -H "Content-Type: application/json" --request PATCH --data '{"quantityOnHand":"220","price":"13.99"}' http://localhost:8080/api/v1/beer/uuid-xxx
     * */
    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        beerService.patchBeerById(beerId, beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Example:
     * curl -H "Content-Type: application/json" --request DELETE http://localhost:8080/api/v1/beer/8a697961-e894-48e0-bf53-d4d2851fb8c1
     * */
    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId) {
        beerService.deleteById(beerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Example:
     * curl -H "Content-Type: application/json" --request PUT --data '{"version":1,"beerName":"Test put beer","beerStyle":"PILSNER","upc":"12345678111","quantityOnHand":"222","price":"11.99"}' http://localhost:8080/api/v1/beer/uuid-xxx
     * */
    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        beerService.updateBeerById(beerId, beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Example:
     * curl -H "Content-Type: application/json" --request POST --data '{"version":1,"beerName":"Test post ber","beerStyle":"PILSNER","upc":"12345678111","quantityOnHand":"222","price":"11.99"}' http://localhost:8080/api/v1/beer
     * */
    @PostMapping(BEER_PATH)
    // @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handlePost(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + savedBeer.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(BEER_PATH)
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
}
