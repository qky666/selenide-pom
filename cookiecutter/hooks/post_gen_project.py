import os

group = '{{ cookiecutter.group }}'
packages = group.split('.')
if len(packages) > 1:
    modules = os.listdir("modules")
    for module in modules:
        kotlin_folder = f"modules/{module}/src/test/kotlin"
        folder = kotlin_folder
        for index in range(len(packages) - 1):
            package = packages[index]
            folder = f"{folder}/{package}"
            os.mkdir(folder)
        packages_folder = f"{kotlin_folder}/{group}"
        new_last_package_folder = f"{folder}/{packages[-1]}"
        os.rename(packages_folder, new_last_package_folder)
