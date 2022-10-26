package command.commands.superuser;

import coffeecord.Util;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class ShutdownCommand implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		TextChannel channel = ctx.getChannel();
		User user = ctx.getAuthor();

		if(Util.isSuperuser(user)) {
			channel.sendMessage("Shutting down...").complete();
			System.exit(0);
		}
	}

	@Override
	public String getCommand() {
		return "shutdown";
	}

	@Override
	public String getHelp() {
		return "Shutdown Bot!\nSuperuser Command!";
	}
}
