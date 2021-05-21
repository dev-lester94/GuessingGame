package corbos.guessinggameapi.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import corbos.guessinggameapi.models.Game;

public class GameFilter extends SimpleBeanPropertyFilter {
    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {

        if(pojo instanceof Game){
            Game game = (Game) pojo;
            if(writer.getName().equals("answer")){
                boolean finished = game.isFinished();
                if(!finished){
                    writer.serializeAsOmittedField(pojo, jgen, provider);
                }else{
                    writer.serializeAsField(pojo,jgen, provider);
                }
            }else{
                writer.serializeAsField(pojo,jgen, provider);
            }
        }
        //super.serializeAsField(pojo, jgen, provider, writer);
    }
}
