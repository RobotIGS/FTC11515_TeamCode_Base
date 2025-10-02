import os
import javalang

def parse_java_files(source_dir):
    classes = []

    for root, _, files in os.walk(source_dir):
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(root, file)
                with open(file_path, 'r', encoding='utf-8') as f:
                    java_code = f.read()
                    tree = javalang.parse.parse(java_code)
                    classes.extend(extract_classes(tree))
    
    return classes

def extract_classes(tree):
    classes = []

    for path, node in tree:
        if isinstance(node, javalang.tree.ClassDeclaration) or isinstance(node, javalang.tree.InterfaceDeclaration):
            class_name = node.name
            class_type = "class" if isinstance(node, javalang.tree.ClassDeclaration) else "interface"
            fields = [format_field(field) for field in node.fields]
            methods = [format_method(method) for method in node.methods]
            classes.append({
                "name": class_name,
                "type": class_type,
                "fields": fields,
                "methods": methods
            })
    
    return classes

def format_field(field):
    visibility = get_visibility(field.modifiers)
    field_type = field.type.name
    field_names = [var.name for var in field.declarators]
    return [f"{visibility} {field_type} {name}" for name in field_names]

def format_method(method):
    visibility = get_visibility(method.modifiers)
    method_name = method.name
    return f"{visibility} {method_name}()"

def get_visibility(modifiers):
    if 'public' in modifiers:
        return '+'
    elif 'protected' in modifiers:
        return '#'
    elif 'private' in modifiers:
        return '-'
    else:
        return '~'  # package-private

def generate_puml(classes, output_file):
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write("@startuml\n")
        for cls in classes:
            f.write(f"{cls['type']} {cls['name']} {{\n")
            for field_list in cls['fields']:
                for field in field_list:
                    f.write(f"  {field}\n")
            for method in cls['methods']:
                f.write(f"  {method}\n")
            f.write("}\n")
        f.write("@enduml\n")

def main(source_dir, output_file):
    classes = parse_java_files(source_dir)
    generate_puml(classes, output_file)
    print(f"PlantUML file generated at {output_file}")

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 3:
        print("Usage: python generate_puml.py <source-directory> <output-file>")
    else:
        source_dir = sys.argv[1]
        output_file = sys.argv[2]
        main(source_dir, output_file)


