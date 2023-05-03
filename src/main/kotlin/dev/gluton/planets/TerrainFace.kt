package dev.gluton.planets

import godot.ArrayMesh
import godot.Mesh
import godot.MeshInstance
import godot.SurfaceTool
import godot.core.PoolIntArray
import godot.core.PoolVector3Array
import godot.core.Vector2
import godot.core.Vector3
import godot.core.times

class TerrainFace(
    private val shapeGenerator: ShapeGenerator,
    private val meshInstance: MeshInstance,
    private val resolution: Int,
    private val localUp: Vector3,
) {
    private val axisA = Vector3(localUp.y, localUp.z, localUp.x)
    private val axisB = localUp.cross(axisA)

    @OptIn(ExperimentalStdlibApi::class)
    fun constructMesh() {
        val vertices = PoolVector3Array()
        val triangles = PoolIntArray()

        for (y in 0..<resolution) {
            for (x in 0..<resolution) {
                // get the offset of the next vertex
                val positionIndex = x + y * resolution
                val percent = Vector2(x, y) / (resolution - 1)
                val pointOnUnitCube = localUp + (percent.x - 0.5) * 2 * axisA + (percent.y - 0.5) * 2 * axisB
                val pointOnUnitSphere = pointOnUnitCube.normalized() // normalize to make a sphere
                vertices.append(shapeGenerator.calculatePointOnPlanet(pointOnUnitSphere))

                // create two triangles (square) from vertex
                if (x != resolution - 1 && y != resolution -1) {
                    triangles.append(positionIndex)
                    triangles.append(positionIndex + resolution)
                    triangles.append(positionIndex + resolution + 1)

                    triangles.append(positionIndex)
                    triangles.append(positionIndex + resolution + 1)
                    triangles.append(positionIndex + 1)
                }
            }
        }

        val surfaceTool = SurfaceTool().apply {
            begin(Mesh.PRIMITIVE_TRIANGLES)
            vertices.forEach(::addVertex)
            triangles.forEach { addIndex(it.toLong()) }
            index()
        }

        meshInstance.mesh = ArrayMesh().apply {
            addSurfaceFromArrays(Mesh.PRIMITIVE_TRIANGLES, surfaceTool.commitToArrays())
        }
    }
}