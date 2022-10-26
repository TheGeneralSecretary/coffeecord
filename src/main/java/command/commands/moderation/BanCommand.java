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

public class BanCommand implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		List<String> args = ctx.getArgs();
		TextChannel channel = ctx.getChannel();
		Message message = ctx.getMessage();
		Member mod = ctx.getAuthorMember();
		Member target = null;
		String reason = "Not Specified";

		if(!mod.hasPermission(Permission.BAN_MEMBERS)) {
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
			channel.sendMessage("Cannot ban yourself").queue();
			return;
		}

		if(args.size() > 1)
			reason = String.join(" ", args.subList(1, args.size()));

		try {
			target.ban(1, reason).queue();
			channel.sendMessage(target.getUser().getAsTag() + " has been banned\nReason: " + reason).queue();
		} catch (InsufficientPermissionException e) {
			channel.sendMessage("Insufficient Bot Permission").queue();
		} catch (HierarchyException e) {
			channel.sendMessage("Can't ban a member with higher or equal highest role than yourself").queue();
		}
	}

	@Override
	public String getCommand() {
		return "ban";
	}

	@Override
	public String getHelp() {
		return "ban [@user] [optional reason]";
	}
}
