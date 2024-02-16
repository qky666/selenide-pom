import os
import shutil


REMOVE_PATHS = [
    {% if cookiecutter.use_cucumber == "yes" %}
    'modules/mtphome/src/test/kotlin/{{ cookiecutter.__group_folder }}/testng',
    'modules/mtphome/src/test/resources/selenide-pom.properties'
    {%- else -%}
    'modules/mtphome/src/test/kotlin/{{ cookiecutter.__group_folder }}/cucumber',
    'modules/mtphome/src/test/resources/features',
    'modules/mtphome/src/test/resources/cucumber.properties',
    'modules/mtphome/src/test/resources/testng-retry.xml',
    'modules/mtphome/src/test/resources/testng-wip.xml'
    {% endif %}
]

for path in REMOVE_PATHS:
    path = path.strip()
    if path and os.path.exists(path):
        if os.path.isdir(path):
            shutil.rmtree(path)
        else:
            os.unlink(path)
