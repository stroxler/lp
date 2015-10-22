# For some reason ssh isn't picking up a definition of JAVA_HOME in
# /etc/profile.d, so I explicitly set it in .bashrc for hadoop purposes.
echo "export JAVA_HOME=$JAVA_HOME" >> $HOME/.bashrc
