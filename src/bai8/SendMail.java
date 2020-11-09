package bai8;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendMail
 */
@WebServlet("/sendMail")
public class SendMail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SendMail() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		String to = "thoainguyen603@gmail.com";
//		String from = "thoainguyen6032@gmail.com";
//		String host = "localhost";
//		Properties properties = System.getProperties();
//		properties.setProperty("mail.smtp.host", host);
//		Session session = Session.getDefaultInstance(properties);// Lấy đối tượng mặc định.

		String to = "thoainguyen603@gmail.com";
		String from = "thoainguyen6032@gmail.com";
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");
		final String username = "thoainguyen6032@gmail.com";
		final String password = "yourpassword";
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);// Tạo đối tượng mặc định MimeMessage.
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			String subject = request.getParameter("subject");
			message.setSubject(subject);

			BodyPart messageBodyPart = new MimeBodyPart();// tạo đối tượng BodyPart của email.
			String content = request.getParameter("content");
			messageBodyPart.setText(content);
			message.setContent(content,"text/html");

			Multipart multipart = new MimeMultipart();// Email sẽ gồm 2 part (text, file attached)
			multipart.addBodyPart(messageBodyPart);// Phần text

			// Phần xử lý với file attached
//			messageBodyPart = new MimeBodyPart();
//			String filename = "file.txt";
//			DataSource source = new FileDataSource(filename);
//			messageBodyPart.setDataHandler(new DataHandler(source));
//			messageBodyPart.setFileName(filename);
//			multipart.addBodyPart(messageBodyPart);
			
//			message.setContent(multipart);

			Transport.send(message); // Gửi mail

			request.setAttribute("message", "Send mail has been done successfully!");
		} catch (Exception e) {
			request.setAttribute("message", "There was an error: " + e.getMessage());
			e.printStackTrace();
		}
		getServletContext().getRequestDispatcher("/messageSendMail").forward(request, response);
		
//		Bật quyền truy cập của ứng dụng kém an toàn cho gmail gửi
//		https://myaccount.google.com/lesssecureapps?pli=1&rapt=AEjHL4OB--K_gPDAinFn4yFin1UBEFpDTFt70SAyLUrU1smWSuNpTi4W2P7EDB5UmgDUZQ1V52QuqLf8ry0xGKNTnIcFOG9VWQ
	}

}
