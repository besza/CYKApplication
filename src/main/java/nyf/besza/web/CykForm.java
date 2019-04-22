package nyf.besza.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CykForm {

    @NotEmpty
    private String input;

    @NotEmpty
    private String grammar;
}
