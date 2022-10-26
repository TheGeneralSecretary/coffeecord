package command.commands.moderation;

import coffeecord.Util;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.List;

public class MuteCommand implements ICommand {
	@Override
	public void execute(CommandContext ctx) {
		List<String> args = ctx.getArgs();
		TextChannel channel = ctx.getChannel();
		Message message = ctx.getMessage();
		Guild guild = ctx.getGuild();
		Member mod = ctx.getAuthorMember();
		Member target = null;
		Role muteRole = null;
		String reason = "Not Specified";

		if(!mod.hasPermission(Permission.getPermissions(4194304L))) {
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
			channel.sendMessage("Cannot mute yourself").queue();
			return;
		}

		muteRole = Util.getMuteRole(guild);
		if(Util.memberHasRole(target, muteRole)) {
			channel.sendMessage(target.getUser().getName() + " is already muted").queue();
			return;
		}

		if(args.size() > 1)
			reason = String.join(" ", args.subList(1, args.size()));

		try {
			guild.addRoleToMember(target, muteRole).queue();
			channel.sendMessage(target.getUser().getAsTag() + " has been Muted\nReason: " + reason).queue();
		} catch (InsufficientPermissionException e) {
			channel.sendMessage("Insufficient Bot Permission").queue();
		} catch (HierarchyException e) {
			channel.sendMessage("Can't mute a member with higher or equal highest role than yourself").queue();
		}
	}

	@Override
	public String getCommand() {
		return "mute";
	}

	@Override
	public String getHelp() {
		return "mute [@User] [Optional reason]";
	}
}
