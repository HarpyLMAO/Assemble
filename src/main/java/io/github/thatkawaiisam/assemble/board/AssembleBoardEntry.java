package io.github.thatkawaiisam.assemble.board;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import lombok.Setter;

public class AssembleBoardEntry {

	private final AssembleBoard board;

	private Team team;
	@Setter
	private String text, identifier;

	/**
	 * Assemble Board Entry
	 *
	 * @param board    that entry belongs to.
	 * @param text     of entry.
	 * @param position of entry.
	 */
	public AssembleBoardEntry(AssembleBoard board, String text, int position) {
		this.board = board;
		this.text = text;
		this.identifier = this.board.getUniqueIdentifier(position);

		this.setup();
	}

	/**
	 * Setup Board Entry.
	 */
	public void setup() {
		final Scoreboard scoreboard = this.board.getScoreboard();

		if (scoreboard == null) {
			return;
		}

		String teamName = this.identifier;

		// This shouldn't happen, but just in case.
		if (teamName.length() > 16) {
			teamName = teamName.substring(0, 16);
		}

		Team team = scoreboard.getTeam(teamName);

		// Register the team if it does not exist.
		if (team == null) {
			team = scoreboard.registerNewTeam(teamName);
		}

		// Add the entry to the team.
		if (!team.getEntries().contains(this.identifier)) {
			team.addEntry(this.identifier);
		}

		// Add the entry if it does not exist.
		if (!this.board.getEntries().contains(this)) {
			this.board.getEntries().add(this);
		}

		this.team = team;
	}

	/**
	 * Send Board Entry Update.
	 *
	 * @param position of entry.
	 */
	public void send(int position) {
		String[] split = this.splitText(text);

		team.set
		team.setPrefix(split[0]);
		team.setSuffix(split[1]);

		// Set the score
		this.board.getObjective().getScore(this.identifier).setScore(position);
	}

	/**
	 * Remove Board Entry from Board.
	 */
	public void remove() {
		this.board.getIdentifiers().remove(this.identifier);
		this.board.getScoreboard().resetScores(this.identifier);
	}


	private String getFirstSplit(String string) {
		return string.length() > 16 ? string.substring(0, 16) : string;
	}

	private String getSecondSplit(String string) {
		if (string.length() > 32) string = string.substring(0, 32);
		return string.length() > 16 ? string.substring(16) : "";
	}

	private String[] splitText(String text) {
		if (text.length() < 17) {
			return new String[]{text, ""};
		} else {
			final String left = text.substring(0, 16);
			final String right = text.substring(16);

			if (left.endsWith("§")) {
				return new String[]{
						left.substring(0, left.toCharArray().length - 1),
						StringUtils.left(ChatColor.getLastColors(left) + "§" + right, 16)
				};
			} else {
				return new String[]{
						left,
						StringUtils.left(ChatColor.getLastColors(left) + right, 16)
				};
			}
		}
	}

}
