# Hadoop requires you be able to ssh to localhost.
# If you already have a set of keys, this is best done manually, but on
# a clean server, this script will generate a new key to set up access.
ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
