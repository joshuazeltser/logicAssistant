package logic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExpressionController {

	    @GetMapping("/expression")
		public String expressionForm(Model model) {
			model.addAttribute("expression", new Expression());
			return "expression";
		}

		@PostMapping("/expression")
		public String expressionSubmit(@ModelAttribute Expression expression) {
	    	return "result";
		}

}
