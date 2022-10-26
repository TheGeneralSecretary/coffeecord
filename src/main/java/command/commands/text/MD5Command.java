package command.commands.text;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Command implements ICommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(MD5Command.class);

	@Override
	public void execute(CommandContext ctx) {
		TextChannel channel = ctx.getChannel();
		Message message = ctx.getMessage();

		if(ctx.getArgs().isEmpty()) {
			channel.sendMessage(getHelp()).queue();
			return;
		}

		String text = message.getContentRaw().replaceAll("(.*)\\bhaste\\b[\\n\\r\\s]+", "");
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(text.getBytes(StandardCharsets.UTF_8));
			channel.sendMessage(String.format("%032x", new BigInteger(1, md5.digest()))).queue();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
	}

	@Override
	public String getCommand() {
		return "md5";
	}

	@Override
	public String getHelp() {
		return "md5 [text]";
	}
}
