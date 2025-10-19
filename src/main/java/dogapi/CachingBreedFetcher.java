package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private final BreedFetcher fetcher;
    Map<String, List<String>> breedCache = new HashMap<>();

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

        List<String> subBreeds;

        if (breedCache.containsKey(breed)) {
            subBreeds = breedCache.get(breed);
        } else {
            try {
                subBreeds = fetcher.getSubBreeds(breed);
                breedCache.put(breed, subBreeds);
            } finally {
                callsMade++;
            }
        }

        return subBreeds;
    }

    public int getCallsMade() {
        return callsMade;
    }
}