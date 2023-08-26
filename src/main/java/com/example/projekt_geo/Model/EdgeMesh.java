package com.example.projekt_geo.Model;

import java.util.Objects;

public class EdgeMesh {
    public int p0;
    public int p1;

    public EdgeMesh(int p0, int p1)
    {
        this.p0 = p0;
        this.p1 = p1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeMesh edgeMesh = (EdgeMesh) o;
        return (p0 == edgeMesh.p0 && p1 == edgeMesh.p1 ) || (p1 == edgeMesh.p0 && p0 == edgeMesh.p1 );
    }

    @Override
    public int hashCode() {
        return Objects.hash(p0, p1);
    }
}
