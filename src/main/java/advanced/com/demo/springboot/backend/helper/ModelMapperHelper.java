package advanced.com.demo.springboot.backend.helper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

public class ModelMapperHelper {
    public static ModelMapper mapperHelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setSkipNullEnabled(true);
        return modelMapper;
    }
}
