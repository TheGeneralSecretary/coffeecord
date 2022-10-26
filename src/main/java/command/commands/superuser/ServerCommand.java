package command.commands.superuser;

import coffeecord.Util;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ServerCommand implements ICommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerCommand.class);

	@Override
	public void execute(CommandContext ctx) {
		List<String> args = ctx.getArgs();
		TextChannel channel = ctx.getChannel();
		User user = ctx.getAuthor();

		if(args.size() < 1) {
			channel.sendMessage(getHelp()).queue();
			return;
		}

		if(Util.isSuperuser(user)) {
			if(args.get(0).equals("ip"))
				channel.sendMessage(getIP()).queue();
			else
				channel.sendMessage(getHelp()).queue();
		}
	}

	@Override
	public String getCommand() {
		return "server";
	}

	@Override
	public String getHelp() {
		return "Server Utility Commands\nSuperuser Command!\nserver [ip]";
	}

	private String getIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LOGGER.error(e.getMessage());
		}
		return "null";
	}
}
