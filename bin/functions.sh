indentation="    "
spin='/-\|'
spincount=${#spin}

# Checks if the current directory is part of a .git repository
function VerifyGitDirectory()
{
    if [[ ! -d .git && ! `git rev-parse --git-dir 2> /dev/null` ]]; then
        PrintError "Not Git directory"
        echo

        exit
    fi;
}

# Checks the given environment variable
#
# ${1} = Name of the environment variable
# ${2} = Description of value
function VerifyEnv()
{
    # Check environment variable.
    envName=${1}
    expectedValueDescription=${2}
    expectedFile=${3}
    export envVar=${envName}
    eval envValue=\$$envVar

    # We need to have the CDT security tools directory for access to the jwt tool.
    if [[ ! -z "${envValue}" ]]; then
        if [[ ! -z "${expectedFile}" ]]; then
            exec 5>&1
            results=$( VerifyFileExist "${expectedFile}" "${envValue}" | tee /dev/fd/5 )
            if [[ -z $results ]]; then
                return
            fi
        fi
    else
        PrintError "${envName} is not set!"
        echo 
    fi

    PrintInfo "Set your ${envName} on the command line with:"
    echo 
    PrintCodeLine "export ${BICyan}${envName}${Cyan}=<${expectedValueDescription}>"
    echo 

    exit
}

# Prints a value intended to be for copying.
#
# ${1} = The value to paste.
function PrintCopyPasta()
{
    pasta=${1}

    # Print the token for output.
    PrintSeparator
    echo
    PrintPasta "${pasta}"
    echo
    PrintSeparator
    echo
}

# Add value to the clipboard.
#
# ${1} = Value to add to the clipboard.
function AddToClipboard()
{
    value=${1}

    # Add the token to the clipboard. Uses printf instead of echo to avoid extra newlines.
    printf "${value}" | pbcopy

    #echo -e "    ${BGreen}» Copied to clipboard.${No_Color}"
    PrintBoldInfo "Copied to clipboard."
    echo
}

# Symlinks a list of files to a target directory.
#
# ${1} = List of absolute paths to files for symlinking.
# ${2} = Target directory to symlink to.
function SymlinkFiles()
{
    srcFiles=${1}
    targetPath=${2}

    # For each runConfiguration in this directory.
    for srcFilePath in ${srcFiles}; do
        file=$(basename ${srcFilePath})
        targetFilePath=${targetPath}/${file}

        echo

        # If the path is actually a symlink.
        if [[ -L ${targetFilePath} ]]; then
            # If it is the right symlink, then update it.
            if [[ ${srcFilePath} = $(readlink ${targetFilePath}) ]]; then
                PrintInfo "Found     ${BIBlue}${file}${Green}"
                rm ${targetFilePath}
                ln -s ${srcFilePath} ${targetFilePath}
            # If it is the wrong symlink, then error out.
            else
                PrintError "  ${BRed}${file}${Red} is a symlink not from this script!"
            fi
            continue
        fi

        # If the path is an actual file, then error out.
        if [[ -f ${targetFilePath} ]]; then
            PrintError "  ${BRed}${file}${Red} is already an actual file!"
            continue
        fi

        # Otherwise this is a new configuration so let's install it.
        ln -s ${srcFilePath} ${targetFilePath}
        PrintBoldInfo "Installed ${BIBlue}${file}${BGreen}"
    done
}

function VerifyFileExist()
{
    file=${1}
    dir=${2}

    if [[ ! -f ${dir}/${file} ]]; then
        PrintError "The file ${BRed}${file}${Red} does not exist in ${BRed}${dir}${Red}!"
        echo
    fi
}

function VerifyFolderExist()
{
    dir=${1}

    if [[ ! -d ${dir} ]]; then
        PrintError "The folder ${BRed}${dir}${Red} does not exist!"
        echo
    fi
}

function ReadInput()
{
    read -p $'\e[31m    Foobar\e[0m: ' foo
}

function ErrorExit
{
	PrintError "$1" 1>&2
	echo
	exit 1
}

function SpinProgress
{
	printf "%s\b" "${spin:i++%spincount:1}"
}

#################### Formatting Functions ####################
function Indent()
{
    indentation="${indentation}    "
}

function Unindent()
{
    indentation="${indentation:0:${#indentation}-4}"
}

function PrintInfo()
{
    info=${1}
    echo -e "${indentation}${Green}» ${info}${No_Color}"
}

function PrintBoldInfo()
{
    info=${1}
    echo -e "${indentation}${BGreen}» ${info}${No_Color}"
}

function PrintCodeLine()
{
    code=${1}
    echo -e "${indentation}    ${Cyan}${code}${No_Color}"
}

function PrintDescription()
{
    description=${1}
    echo -e "${indentation}    ${description}"
}

function PrintWarning()
{
    warning=${1}
    echo -e "${indentation}${BYellow}» ${warning}${No_Color}" | tr -d '\n'
    echo
}

function PrintSeparator()
{
    echo -e "${Purple}--------------------------------------------------${No_Color}"
}

function PrintPasta()
{
    pasta=${1}
    echo -e "${pasta}"
}

function PrintError()
{
    error=${1}
    echo -e "${indentation}${Red}» ${White}${On_Red}[ERROR]${Red} ${error}${No_Color}"
}
