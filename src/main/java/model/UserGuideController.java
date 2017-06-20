package model;

import org.apache.maven.surefire.shade.org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class UserGuideController {

	    @GetMapping("/userGuide")
		public void expressionForm(Model model) {

		}

		@PostMapping("/userGuide")
		public void expressionSubmit() {

		}


	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public void getFile(HttpServletResponse response) {
		try {
			DefaultResourceLoader loader = new DefaultResourceLoader();
			InputStream is = loader.getResource("static/userGuide.pdf").getInputStream();
			IOUtils.copy(is, response.getOutputStream());
			response.setHeader("Content-Disposition", "attachment; filename=userGuide.pdf");
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}

}
