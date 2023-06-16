import os
import shutil


REMOVE_PATHS = [
    {% if cookiecutter.use_cucumber == "yes" %}
    'src/test/kotlin/{{ cookiecutter.__group_folder }}/testng',
    'src/test/resources/selenide-pom.properties'
    {%- else -%}
    'src/test/kotlin/{{ cookiecutter.__group_folder }}/cucumber',
    'src/test/resources/features',
    'src/test/resources/cucumber.properties'
    {% endif %}
]

for path in REMOVE_PATHS:
    path = path.strip()
    if path and os.path.exists(path):
        if os.path.isdir(path):
            shutil.rmtree(path)
        else:
            os.unlink(path)
