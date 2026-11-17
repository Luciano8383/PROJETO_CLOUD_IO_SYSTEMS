"""Configuração PostgreSQL do CiênciaD, carregada do arquivo .env local."""
import os                                                      # import: os = lê variáveis de ambiente
from pathlib import Path                                       # import: Path = trabalha com caminhos de arquivos

from dotenv import load_dotenv                                 # import: load_dotenv lê o arquivo .env
from sqlalchemy.engine import URL, make_url                   # URL monta e interpreta endereços PostgreSQL

load_dotenv(Path(__file__).resolve().parent / ".env")   # lê o .env ao lado deste arquivo


def database_url() -> URL:                                     # função: devolve o endereço de conexão com o banco
    """Usa a URL Neon pooled, a URL direta ou os campos DB_*."""
    raw_url = os.getenv("DATABASE_URL_POOLED") or os.getenv("DATABASE_URL")
    if raw_url:
        url = make_url(raw_url)
        if url.drivername in {"postgres", "postgresql"}:
            url = url.set(drivername="postgresql+psycopg")
        if url.drivername != "postgresql+psycopg":
            raise ValueError("DATABASE_URL deve usar o esquema PostgreSQL.")
        return url

    required = ("DB_HOST", "DB_NAME", "DB_USER", "DB_PASSWORD")
    missing = [key for key in required if not os.getenv(key)]
    if missing:
        raise ValueError(f"Variáveis de banco ausentes: {', '.join(missing)}")

    try:
        port = int(os.getenv("DB_PORT", "5432"))
    except ValueError as exc:
        raise ValueError("DB_PORT deve ser um número inteiro.") from exc

    return URL.create(                                         # monta o endereço do PostgreSQL
        "postgresql+psycopg",                                  # driver: psycopg (conversa com o PostgreSQL)
        username=os.environ["DB_USER"],
        password=os.environ["DB_PASSWORD"],
        host=os.environ["DB_HOST"],
        port=port,
        database=os.environ["DB_NAME"],
        query={"sslmode": os.getenv("DB_SSLMODE", "require")},
    ) 