package dm.dracolich.mtgLibrary.web.helpers;

import dm.dracolich.forge.error.ApiError;
import dm.dracolich.forge.exception.ResponseException;
import dm.dracolich.mtgLibrary.dto.error.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ErrorUtil {
    public static ResponseException notFound(String context, String entity, String identifier) {
        var error = new ApiError(ErrorCodes.DMD021);
        return new ResponseException(ErrorCodes.DMD021.format(context, entity, identifier),
                List.of(error), HttpStatus.NOT_FOUND);
    }
}
