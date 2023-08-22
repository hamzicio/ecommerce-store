package ecommerce.api.utility;

import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ecommerce.api.entity.CustomerOrder;
import ecommerce.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Component
public class MailConstructor {
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private Environment env;

	
	public MimeMessagePreparator constructOrderConfirmationEmail (User user, CustomerOrder order, Locale locale) {
		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("user", user);
		context.setVariable("cartItemList", order.getProducts());
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getEmail());
				email.setSubject("Order Confirmation - "+order.getOrderId());
				email.setText(text, true);
				email.setFrom(new InternetAddress("ecommerce_store_new@gmail.com"));
			}
		};
		
		return messagePreparator;
	}

	public MimeMessagePreparator constructOrderCancelEmail (User user, CustomerOrder order, Locale locale) {
		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("user", user);;
		String text = templateEngine.process("orderCancelEmailTemplate", context);

		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getEmail());
				email.setSubject("Order Cancellation - "+order.getOrderId());
				email.setText(text, true);
				email.setFrom(new InternetAddress("ecommerce_store_new@gmail.com"));
			}
		};

		return messagePreparator;
	}
	
	
}
