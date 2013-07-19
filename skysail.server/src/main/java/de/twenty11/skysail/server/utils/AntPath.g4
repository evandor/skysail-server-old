grammar AntPath;

antPathMatcher : SLASH chars WS? ;
chars          : ('a-z'|'A-Z'|US|SLASH|QUEST|STAR)+ ;        
US             : '_' ;
STAR           : '*' ;
SLASH          : '/' ;
QUEST          : '?' ;
WS             : [ \t\n\r]+ ;
