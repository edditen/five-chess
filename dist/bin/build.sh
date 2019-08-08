#!/usr/bin/env bash

BINDIR="${BASH_SOURCE-$0}"
BINDIR="$(dirname "${BINDIR}")"
BASEDIR="$(cd "${BINDIR}"/..; pwd)"

## clear work
rm -rf "$BASEDIR"/classes/*
rm -rf "$BASEDIR"/lib/*

cd "$BASEDIR"/../chess-server
mvn clean package -DskipTests
cp -r target/classes/* "$BASEDIR"/classes/
cp -r target/lib/* "$BASEDIR"/lib/
