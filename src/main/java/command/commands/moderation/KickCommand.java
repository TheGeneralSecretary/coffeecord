package command.commands.moderation;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.List;

public class KickCommand implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		List<String> args = ctx.getArgs();
		TextChannel channel = ctx.getChannel();
		Message message = ctx.getMessage();
		Member mod = ctx.getAuthorMember();
		Member target = null;
		String reason = "Not Specified";

		if(!mod.hasPermission(Permission.KICK_MEMBERS)) {
			channel.sendMessage("Insufficient Permission").queue();
			return;
		}

		if(args.size() < 1) {
			channel.sendMessage(getHelp()).queue();
			return;
		}

		if(message.getMentionedMembers().isEmpty()) {
			channel.sendMessage("User is not in this guild").queue();
			return;
		}

		target = message.getMentionedMembers().get(0);
		if(target.getId().equals(mod.getId())) {
			channel.sendMessage("Cannot kick yourself").queue();
			return;
		}

		if(args.size() > 1)
			reason = String.join(" ", args.subList(1, args.size()));

		try {
			target.kick().queue();
			channel.sendMessage(target.getUser().getAsTag() + " has been kicked\nReason: " + reason).queue();
		} catch(InsufficientPermissionException e) {
			channel.sendMessage("Insufficient Bot Permission").queue();
		} catch (HierarchyException e) {
			channel.sendMessage("Can't kick a member with equal or higher role than yourself").queue();
		}
	}

	@Override
	public String getCommand() {
		return "kick";
	}

	@Override
	public String getHelp() {
		return "kick [@user] [optional reason]";
	}
}
