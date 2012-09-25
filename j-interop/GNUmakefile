Default: all

TOP=..
SRC=src

include $(TOP)/common.mk

CLASSPATH="$(CLASSLIB)$(CLN)$(LIB)$(CLN)$(SRC)"

include classes.mk

CLASS_FILES:=$(foreach class, $(CLASSES), $(BUILD)/$(subst .,/,$(class)).class)
PACKAGES=$(sort $(basename $(CLASSES)))
PACKAGEDIRS=$(subst .,/,$(PACKAGES))

all: $(LIBDIR)/j-interop.jar $(DOCS)

install: all
	cp $(LIBDIR)/j-interopdeps.jar $(JOVAL)/components/sdk/plugin/remote/rsrc/lib
	cp $(LIBDIR)/j-interop.jar $(JOVAL)/components/sdk/plugin/remote/rsrc/lib

$(LIBDIR)/j-interop.jar: classes properties
	$(JAR) cvf $@ -C $(BUILD)/ .

$(LIBDIR)/j-interopdeps.jar:
	@$(MAKE) --keep-going --directory=../j-interopdeps all

$(DOCS): $(CLASS_FILES)
	mkdir -p $@
	$(JAVA_HOME)/bin/javadoc -d $@ -classpath $(CLASSPATH) $(PACKAGES)

clean:
	@$(MAKE) --keep-going --directory=../j-interopdeps clean
	rm -rf $(LIBDIR)/j-interop.jar
	rm -rf $(DOCS)
	rm -rf $(BUILD)

classes: $(LIBDIR)/j-interopdeps.jar classdirs $(CLASS_FILES)

properties:
	cp $(SRC)/org/jinterop/dcom/jierrormessages.properties $(BUILD)/org/jinterop/dcom

classdirs: $(foreach pkg, $(PACKAGEDIRS), $(BUILD)/$(pkg)/)

$(BUILD)/%.class: $(SRC)/%.java
	$(JAVAC) $(JAVACFLAGS) -d $(BUILD) -classpath $(CLASSPATH) $<

$(BUILD)/%/:
	mkdir -p $(subst PKG,,$@)
