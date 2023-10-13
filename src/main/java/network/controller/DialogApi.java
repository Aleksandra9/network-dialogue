package network.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import network.dto.DialogueMessage;
import network.dto.ErrorModel;
import network.dto.NewMessageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Validated
public interface DialogApi {
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Диалог между двумя пользователями", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DialogueMessage.class)))),
        @ApiResponse(responseCode = "400", description = "Невалидные данные ввода"),
        @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "503", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorModel.class))) })
    @RequestMapping(value = "/dialog/{user_id}/list", produces = {"application/json"}, method = RequestMethod.GET)
    ResponseEntity<List<DialogueMessage>> dialogUserIdListGet(@Parameter(in = ParameterIn.PATH, required=true, schema=@Schema()) @PathVariable("user_id") String userId, Principal principal);

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Успешно отправлено сообщение"),
        @ApiResponse(responseCode = "400", description = "Невалидные данные ввода"),
        @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
        @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "503", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorModel.class))) })
    @RequestMapping(value = "/dialog/{user_id}/send", produces = {"application/json"}, consumes = { "application/json" }, method = RequestMethod.POST)
    ResponseEntity<Void> dialogUserIdSendPost(@Parameter(in = ParameterIn.PATH, required=true, schema=@Schema()) @PathVariable("user_id") String userId, @Parameter(in = ParameterIn.DEFAULT, schema=@Schema()) @Valid @RequestBody NewMessageModel body, Principal principal);
}

