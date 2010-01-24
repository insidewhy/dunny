default: run

dunny: Dunny.scala
	fsc $^

raw:
	@scala -classpath . org.chilon.dunny.Dunny

run: dunny
	@scala -classpath . org.chilon.dunny.Dunny | play 2> /dev/null -t raw -r 44100 -B -b 64 -e float -
