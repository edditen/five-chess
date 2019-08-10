#!/usr/bin/env bash

BIN_DIR="${BASH_SOURCE-$0}"
BIN_DIR="$(dirname "${BIN_DIR}")"
BASE_DIR="$(cd "${BIN_DIR}"/..; pwd)"

# mkdirs
mkdir -p "$BASE_DIR"/classes
mkdir -p "$BASE_DIR"/lib
mkdir -p "$BASE_DIR"/logs

## clear work
rm -rf "$BASE_DIR"/classes/*
rm -rf "$BASE_DIR"/lib/*

cd "$BASE_DIR"/../chess-server
mvn clean package -DskipTests
cp -r target/classes/* "$BASE_DIR"/classes/
cp -r target/lib/* "$BASE_DIR"/lib/
