// Generated from de\twenty11\skysail\server\u005Cutils\AntPath.g4 by ANTLR 4.1
package de.twenty11.skysail.server.utils;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AntPathParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__1=1, T__0=2, US=3, STAR=4, SLASH=5, QUEST=6, WS=7;
	public static final String[] tokenNames = {
		"<INVALID>", "'a-z'", "'A-Z'", "'_'", "'*'", "'/'", "'?'", "WS"
	};
	public static final int
		RULE_antPathMatcher = 0, RULE_chars = 1;
	public static final String[] ruleNames = {
		"antPathMatcher", "chars"
	};

	@Override
	public String getGrammarFileName() { return "AntPath.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public AntPathParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class AntPathMatcherContext extends ParserRuleContext {
		public CharsContext chars() {
			return getRuleContext(CharsContext.class,0);
		}
		public TerminalNode WS() { return getToken(AntPathParser.WS, 0); }
		public TerminalNode SLASH() { return getToken(AntPathParser.SLASH, 0); }
		public AntPathMatcherContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_antPathMatcher; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AntPathListener ) ((AntPathListener)listener).enterAntPathMatcher(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AntPathListener ) ((AntPathListener)listener).exitAntPathMatcher(this);
		}
	}

	public final AntPathMatcherContext antPathMatcher() throws RecognitionException {
		AntPathMatcherContext _localctx = new AntPathMatcherContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_antPathMatcher);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(4); match(SLASH);
			setState(5); chars();
			setState(7);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(6); match(WS);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CharsContext extends ParserRuleContext {
		public TerminalNode SLASH(int i) {
			return getToken(AntPathParser.SLASH, i);
		}
		public List<TerminalNode> US() { return getTokens(AntPathParser.US); }
		public TerminalNode QUEST(int i) {
			return getToken(AntPathParser.QUEST, i);
		}
		public List<TerminalNode> QUEST() { return getTokens(AntPathParser.QUEST); }
		public List<TerminalNode> STAR() { return getTokens(AntPathParser.STAR); }
		public TerminalNode STAR(int i) {
			return getToken(AntPathParser.STAR, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(AntPathParser.SLASH); }
		public TerminalNode US(int i) {
			return getToken(AntPathParser.US, i);
		}
		public CharsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_chars; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AntPathListener ) ((AntPathListener)listener).enterChars(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AntPathListener ) ((AntPathListener)listener).exitChars(this);
		}
	}

	public final CharsContext chars() throws RecognitionException {
		CharsContext _localctx = new CharsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_chars);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(10); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(9);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 1) | (1L << 2) | (1L << US) | (1L << STAR) | (1L << SLASH) | (1L << QUEST))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				}
				setState(12); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 1) | (1L << 2) | (1L << US) | (1L << STAR) | (1L << SLASH) | (1L << QUEST))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\t\21\4\2\t\2\4\3"+
		"\t\3\3\2\3\2\3\2\5\2\n\n\2\3\3\6\3\r\n\3\r\3\16\3\16\3\3\2\4\2\4\2\3\3"+
		"\2\3\b\20\2\6\3\2\2\2\4\f\3\2\2\2\6\7\7\7\2\2\7\t\5\4\3\2\b\n\7\t\2\2"+
		"\t\b\3\2\2\2\t\n\3\2\2\2\n\3\3\2\2\2\13\r\t\2\2\2\f\13\3\2\2\2\r\16\3"+
		"\2\2\2\16\f\3\2\2\2\16\17\3\2\2\2\17\5\3\2\2\2\4\t\16";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}