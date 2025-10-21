package com.bashar.avalag.src.core.data.local.room.dao

/*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bashar.avalag.src.core.data.local.room.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(project: ProjectEntity): Long

    @Update
    fun update(project: ProjectEntity): Int

    @Delete
    fun delete(project: ProjectEntity)

    @Query("SELECT * FROM projects")
    fun getProjects(): List<ProjectEntity>


    @get:Query("SELECT * FROM projects")
    val allProjects: Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE projectId = :projectId")
    fun getProjectById(projectId: Long): Flow<ProjectEntity>
}*/
