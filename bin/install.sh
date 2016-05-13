#!/usr/local/bin/bash

# This gets the path of the executing script.
SELF_PATH=$(cd -P -- "$(dirname -- "${0}")" && pwd -P) && SELF_PATH=${SELF_PATH}/$(basename -- "${0}")
# If the path is a symlink (which is likely if it is in /usr/local/bin) this will get the actual filepath.
REAL_FILE=$(readlink ${SELF_PATH})
# If it isn't a symlink then let's stick with SELF_PATH
if [[ -z $REAL_FILE ]]; then
    REAL_FILE=${SELF_PATH}
fi
REAL_DIR=$(dirname ${REAL_FILE})

source $REAL_DIR/colors.sh
source $REAL_DIR/functions.sh

# Start with a blank line.
echo


##################################################
# Provide help documentation
##################################################

# Provide help.
getopts h help
if [[ "${help}" = "h" ]]; then
    PrintInfo "Description"
    echo
    PrintDescription "Installs the following:"
    echo
    Indent
    PrintDescription " - Utility bash scripts to ${BIBlue}/usr/local/bin${No_Color}"
    Unindent
    echo
    PrintInfo "Usage"
    echo
    PrintCodeLine "${0} [-h]"
    echo
    PrintInfo "Examples"
    echo
    PrintCodeLine "${0}"
    PrintCodeLine "${0} -h"
    echo

    exit
fi

# Install all utility bash scripts.
PrintInfo "Installing bash utility scripts to ${BGreen}/usr/local/bin${Green}."
Indent
findCommand="find \`pwd\` -type f \( -name \"capi.install\" -o -name \"capi.delete\" -o -name \"capi.bootstrap\" \)"
files=$(eval ${findCommand})
SymlinkFiles "${files}" "/usr/local/bin"
Unindent
echo
