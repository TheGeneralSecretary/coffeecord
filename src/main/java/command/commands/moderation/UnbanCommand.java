package command.commands.moderation;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.List;

public class UnbanCommand implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		List<String> args = ctx.getArgs();
		TextChannel channel = ctx.getChannel();
		Guild guild = ctx.getGuild();
		Member mod = ctx.getAuthorMember();
		String targetUserId;

		if(!mod.hasPermission(Permission.BAN_MEMBERS)) {
			channel.sendMessage("Insufficient Permission").queue();
			return;
		}

		if(args.size() < 1) {
			channel.sendMessage(getHelp()).queue();
			return;
		}

		try {
			targetUserId = args.get(0);
			guild.unban(targetUserId)
					.queue(
							success -> channel.sendMessage("User " + targetUserId + " has been unbanned").queue(),
							failure -> channel.sendMessage("User " + targetUserId + " is not banned").queue()
					);
		} catch (InsufficientPermissionException e) {
			channel.sendMessage("Insufficient Bot Permission").queue();
		} catch (IllegalArgumentException e) {
			channel.sendMessage("UserId can only contain numbers").queue();
		}
	}

	@Override
	public String getCommand() {
		return "unban";
	}

	@Override
	public String getHelp() {
		return "unban [userId]";
	}
}
