dunny: Dunny.scala
	fsc $^

run:
	@scala -classpath . org.chilon.dunny.Dunny | play -t raw -r 44100 -b 32 -e float -
