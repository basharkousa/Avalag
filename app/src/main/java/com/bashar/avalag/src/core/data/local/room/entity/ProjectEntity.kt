package com.bashar.avalag.src.core.data.local.room.entity

/*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "Projects")
data class ProjectEntity(
    @PrimaryKey() val projectId: String,
    val projectName: String,
    val description: String,
    val progress: Double,
    @ColumnInfo(name = "start_date",) val startDate: Long?,
    @ColumnInfo(name = "due_date") val dueDate: Long?
){
    companion object {
         fun from(project: ProjectModel): ProjectEntity {
             println("Project ${project}")
             return ProjectEntity(
                 projectId = project.id?:"${Random.nextDouble()}",
                 projectName = project.name?:"name",
                 description = project.description?:"description",
                 dueDate = project.dueDate?:0,
                 startDate = project.startDate?:0,
                 progress = project.progress?:0.0
             )
         }
    }

        fun toProject(): ProjectModel{
            return ProjectModel(
               id = projectId,
               name = projectName,
               description = description,
                startDate = startDate,
                dueDate = dueDate,
                progress = progress
            )
        }


}*/
