import json
import os

# Configura rutas relativas desde la raíz del proyecto
PROJECT_ROOT = os.path.dirname(os.path.dirname(os.path.dirname(__file__)))
INPUT_PATH = os.path.join(PROJECT_ROOT, "proyecto-programacion", "src", "main", "resources", "textures.cfg")
OUTPUT_PATH = os.path.join(PROJECT_ROOT, "proyecto-programacion", "src", "main", "resources", "textures.json")

def main():
    if not os.path.exists(INPUT_PATH):
        print(f"Error: No existe {INPUT_PATH}")
        return

    entries = []
    with open(INPUT_PATH, "r") as f:
        for line_num, line in enumerate(f, 1):
            line = line.strip()
            if not line or line.startswith("#"):
                continue

            if ":" not in line:
                print(f"Línea {line_num}: Formato inválido (sin ':') -> '{line}'")
                continue

            id_part, path = line.split(":", 1)
            try:
                entries.append({
                    "id": int(id_part.strip()),
                    "path": path.strip()
                })
            except ValueError:
                print(f"Línea {line_num}: ID no numérico -> '{id_part}'")

    with open(OUTPUT_PATH, "w") as f:
        json.dump({"textures": entries}, f, indent=2, ensure_ascii=False)

    print(f"✅ JSON generado en: {OUTPUT_PATH}")
    print(f"   Texturas procesadas: {len(entries)}")

if __name__ == "__main__":
    main()