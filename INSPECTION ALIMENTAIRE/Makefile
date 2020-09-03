venv: venv/bin/activate

.PHONY: default

default: venv/bin/activate

venv/bin/activate: requirements.txt
	virtualenv -p python3 venv
	venv/bin/pip install -Ur requirements.txt
