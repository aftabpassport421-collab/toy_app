#!/bin/sh

#
# Copyright © 2015-2021 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License me standard under the License.
#

##############################################################################
#
# Gradle start up script for POSIX-compliant shell
#
##############################################################################

# Attempt to set APP_HOME

# Resolve links: $0 may be a link
app_path=$0

# Need this for relative symlinks.
while [ -h "$app_path" ]; do
    ls=$( ls -ld "$app_path" )
    link=${ls#*' -> '}
    case $link in
      /*) app_path=$link ;;
      *) app_path=$(dirname "$app_path")/"$link" ;;
    esac
done

# This is normally unused by default, but can be customized by the wrapper task.
APP_BASE_NAME=$(basename "$0")
APP_HOME=$(cd "$(dirname "$app_path")" && pwd -P)

# Use the maximum available RAM but diminish for a smaller footprint
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
JAVA_OPTS=""
GRADLE_OPTS=""

# Find java.exe
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD=$JAVA_HOME/jre/sh/java
    else
        JAVACMD=$JAVA_HOME/bin/java
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD=java
    if ! command -v java >/dev/null 2>&1; then
        die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
fi

# Increase the maximum file descriptors if we can.
case "$(uname)" in
    Darwin* | *BSD* )
        if ulimit -H -n >/dev/null 2>&1; then
            MAX_FD=$(ulimit -H -n)
            ulimit -n "$MAX_FD"
        fi
        ;;
esac

# For Darwin, add options to set process name
if [ "$(uname)" = "Darwin" ] ; then
    JAVA_OPTS="$JAVA_OPTS -Xdock:name=$APP_BASE_NAME -Xdock:icon=$APP_HOME/media/gradle.icns"
fi

# For Cygwin or MSYS, switch paths to Windows format before running java
case "$(uname)" in
    CYGWIN* | MSYS* | MINGW* )
        APP_HOME=$(cygpath --path --mixed "$APP_HOME")
        JAVACMD=$(cygpath --unix "$JAVACMD")
        ;;
esac

# Escape application arguments
save () {
    for arg do
        printf "%s\n" "$arg" | sed -e 's/'"'"'/'"'"'\\"'"'"'/g' -e 's/^/'"'"'/' -e 's/$/'"'"'/'
    done
}

# Collect all arguments for the java command
set -- \
    "-Dorg.gradle.appname=$APP_BASE_NAME" \
    -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"

exec "$JAVACMD" "$@"
