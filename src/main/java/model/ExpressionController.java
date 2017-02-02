package model;

import model.Expression;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExpressionController {

	    @GetMapping("/")
		public String expressionForm(Model model) {
			model.addAttribute("proof", new Proof());
			return "proof";
		}

		@PostMapping("/")
		public String expressionSubmit(@ModelAttribute Proof proof) {
	    	return "proof";
		}

}
