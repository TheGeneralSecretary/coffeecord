package command.commands.text;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HasteCommand implements ICommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(HasteCommand.class);

	@Override
	public void execute(CommandContext ctx) {
		List<String> args = ctx.getArgs();
		TextChannel channel = ctx.getChannel();
		Message textMessage = ctx.getMessage();
		String rawText = null;
		String mdSanitizedText = null;

		if(args.isEmpty()) {
			channel.sendMessage(getHelp()).queue();
			return;
		}

		// Handle Content Raw
		rawText = textMessage.getContentRaw().replaceAll("(.*)\\bhaste\\b[\\n\\r\\s]+", "");
		String formattedText = MarkdownSanitizer.sanitize(rawText);

		try {
			String pasteLink = createPaste(formattedText);
			channel.sendMessage(pasteLink).queue();
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public String getCommand() {
		return "haste";
	}

	@Override
	public String getHelp() {
		return "haste [text]";
	}

	private String createPaste(String text) throws IOException {
		String response = null;
		String requestURL = "https://hastebin.com/documents";
		byte[] data = text.getBytes(StandardCharsets.UTF_8);
		int dataLen = data.length;
		DataOutputStream os;

		URL url = new URL(requestURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Length", Integer.toString(dataLen));
		conn.setUseCaches(false);

		try {
			os = new DataOutputStream(conn.getOutputStream());
			os.write(data);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			response = br.readLine();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		if(response.contains("\"key\"")) {
			response = response.substring(response.indexOf(":") + 2, response.length() - 2);
			response = requestURL + "/" + response;
		}

		return response.replace("/documents", "");
	}
}
