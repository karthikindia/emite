#!/bin/bash

PARAMS=$#
NAME=$2
DIR=$1

# CORRECT PARAMS ###############################################################

  if [ $PARAMS -ne 2 ]
  then
  	echo "Use: $0 <packageDirDest> <ClassName>"
    echo "$0 src/main/java/com/calclab/examplechat/client/chatuiplugin/chat ChatUI"
    exit
  fi

# DO
PACKAGE=`echo $DIR | cut -d "/" -f 4- | sed 's/\//\./g'`

cat <<EOF > $DIR/${NAME}.java
package $PACKAGE;

public interface $NAME {

}
EOF

cat <<EOF > $DIR/${NAME}Presenter.java
package $PACKAGE;

import org.ourproject.kune.platf.client.View;

public class ${NAME}Presenter implements $NAME {

private ${NAME}View view;

public ${NAME}Presenter() {
}

public void init(${NAME}View view) {
this.view = view;
}


    public View getView() {
        return view;
    }

}

EOF

cat <<EOF > $DIR/${NAME}View.java
package $PACKAGE;

import org.ourproject.kune.platf.client.View;

public interface ${NAME}View extends View {
}

EOF

cat <<EOF > $DIR/${NAME}Panel.java
package $PACKAGE;

public class ${NAME}Panel implements ${NAME}View {

public ${NAME}Panel(final ${NAME}Presenter presenter) {
}
}
EOF


cat <<EOF
// If you use a Factory, paste this
package $PACKAGE;

import $PACKAGE.${NAME}Presenter;
import $PACKAGE.${NAME}Panel;
import $PACKAGE.${NAME};

public ${NAME} create${NAME}() {
${NAME}Presenter presenter = new ${NAME}Presenter();
${NAME}Panel panel = new ${NAME}Panel(presenter);
presenter.init(panel);
return presenter;
}
EOF
