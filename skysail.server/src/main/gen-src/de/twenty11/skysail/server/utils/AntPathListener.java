// Generated from de\twenty11\skysail\server\u005Cutils\AntPath.g4 by ANTLR 4.1
package de.twenty11.skysail.server.utils;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AntPathParser}.
 */
public interface AntPathListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AntPathParser#chars}.
	 * @param ctx the parse tree
	 */
	void enterChars(@NotNull AntPathParser.CharsContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntPathParser#chars}.
	 * @param ctx the parse tree
	 */
	void exitChars(@NotNull AntPathParser.CharsContext ctx);

	/**
	 * Enter a parse tree produced by {@link AntPathParser#antPathMatcher}.
	 * @param ctx the parse tree
	 */
	void enterAntPathMatcher(@NotNull AntPathParser.AntPathMatcherContext ctx);
	/**
	 * Exit a parse tree produced by {@link AntPathParser#antPathMatcher}.
	 * @param ctx the parse tree
	 */
	void exitAntPathMatcher(@NotNull AntPathParser.AntPathMatcherContext ctx);
}