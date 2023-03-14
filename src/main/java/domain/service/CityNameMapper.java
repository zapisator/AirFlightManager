package domain.service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CityNameMapper {
    private final Map<String, String> cities = Map.of(
            "VVO", "Vladivostok",
            "Владивосток", "Vladivostok",
            "TLV", "Tel_Aviv",
            "Тель-Авив", "Tel_Aviv"
            );
    private final Set<String> correctNames = Set.of(
            "Vladivostok",
            "Tel_Aviv"
    );

    public String correctCityOrThrow(String city) {
        return correctNames.contains(city)
                ? city
                : Optional
                .ofNullable(cities.get(city))
                .orElseThrow(() -> new RuntimeException(
                        "Такого города в системе не определено. Обратитесь в службу поддержики"
                ));
    }

}
