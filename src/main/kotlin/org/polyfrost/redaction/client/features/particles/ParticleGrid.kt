package org.polyfrost.redaction.client.features.particles

class ParticleGrid(
    val width: Int,
    val height: Int,
    private val cellSize: Int,
) {
    private val columns = (width / cellSize).coerceAtLeast(1)
    private val rows = (height / cellSize).coerceAtLeast(1)
    private val cells = Array(rows * columns) { IntArray(0) }

    fun rebuild(particles: Collection<Particle>) {
        // Clear cells
        for (i in cells.indices) {
            cells[i] = IntArray(0)
        }

        // Count
        val counts = IntArray(rows * columns)
        for (particle in particles) {
            val index = indexOf(particle)
            counts[index]++
        }

        // Alloc
        for (i in cells.indices) {
            cells[i] = IntArray(counts[i])
        }

        // Fill
        counts.fill(0)
        for ((i, particle) in particles.withIndex()) {
            val index = indexOf(particle)
            val pos = counts[index]++
            cells[index][pos] = i
        }
    }

    fun query(x: Int, y: Int, fastStorage: FastIntArrayList): IntArray {
        val cellX = (x / cellSize).toInt().coerceIn(0, columns - 1)
        val cellY = (y / cellSize).toInt().coerceIn(0, rows - 1)
        fastStorage.clear()

        val minX = (cellX - 1).coerceAtLeast(0)
        val maxX = (cellX + 1).coerceAtMost(columns - 1)
        val minY = (cellY - 1).coerceAtLeast(0)
        val maxY = (cellY + 1).coerceAtMost(rows - 1)
        for (gy in minY..maxY) {
            for (gx in minX..maxX) {
                val cell = gy * columns + gx
                val arr = cells[cell]
                if (arr.isNotEmpty()) {
                    fastStorage.addAll(arr)
                }
            }
        }

        return fastStorage.toIntArray()
    }

    private fun indexOf(particle: Particle): Int {
        val cx = (particle.x / cellSize).toInt().coerceIn(0, columns - 1)
        val cy = (particle.y / cellSize).toInt().coerceIn(0, rows - 1)
        return cy * columns + cx
    }
}
