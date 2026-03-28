package com.oliver.fuzzy.energic.joy.validator;

import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import com.oliver.fuzzy.energic.joy.model.dto.SensorDTO;
import com.oliver.fuzzy.energic.joy.util.Constants;
import org.springframework.stereotype.Component;

@Component
public class SensorValidator implements Validator<SensorDTO> {

    @Override
    public void validate(SensorDTO object) throws ValidationException {
        if(object == null){
            throw new ValidationException(Constants.Validaiton.SENSOR_DATA_REQUIRED);
        }

        if(object.getId() == null){
            throw new ValidationException(Constants.Validaiton.SENSOR_ID_REQUIRED);
        }

        if(object.getCityName() == null || object.getCityName().isBlank()){
            throw new ValidationException(Constants.Validaiton.SENSOR_CITY_NAME);
        }

        if(object.getCountryName() == null || object.getCountryName().isBlank()){
            throw new ValidationException(Constants.Validaiton.SENSOR_COUNTRY_NAME);
        }
    }
}
