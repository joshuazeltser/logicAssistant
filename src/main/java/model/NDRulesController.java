package model;

import org.apache.maven.surefire.shade.org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class NDRulesController {

	@RequestMapping(value = "/ND-Rules", method = RequestMethod.GET)
	public void getFile(HttpServletResponse response) {
		try {
			DefaultResourceLoader loader = new DefaultResourceLoader();
			InputStream is = loader.getResource("static/NDrules.pdf").getInputStream();
			IOUtils.copy(is, response.getOutputStream());
			response.setHeader("Content-Disposition", "attachment; filename=NDrules.pdf");
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}

}
