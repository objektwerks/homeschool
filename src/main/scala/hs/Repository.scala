package hs

import java.sql.{Date, Timestamp}
import java.time.{LocalDate, LocalDateTime}

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class Repository(val config: DatabaseConfig[JdbcProfile], val profile: JdbcProfile, val awaitDuration: Duration = 1 second) {
  import profile.api._

  implicit val dateMapper = MappedColumnType.base[LocalDate, Date](ld => Date.valueOf(ld),d => d.toLocalDate)
  implicit val dateTimeMapper = MappedColumnType.base[LocalDateTime, Timestamp](ldt => Timestamp.valueOf(ldt), ts => ts.toLocalDateTime)
  val schema = schools.schema ++ courses.schema ++ students.schema ++ grades.schema ++ assignments.schema
  val db = config.db

  def await[T](action: DBIO[T]): T = Await.result(db.run(action), awaitDuration)
  def exec[T](action: DBIO[T]): Future[T] = db.run(action)

  def closeRepository() = db.close()

  def createSchema() = await(DBIO.seq(schema.create))
  def dropSchema() = await(DBIO.seq(schema.drop))

  case class School(id: Int = 0, name: String, website: Option[String] = None)
  class Schools(tag: Tag) extends Table[School](tag, "schools") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.Unique)
    def website = column[Option[String]]("website")
    def * = (id, name, website) <> (School.tupled, School.unapply)
  }
  object schools extends TableQuery(new Schools(_)) {
    val compiledList = Compiled { sortBy(_.name.asc) }
    def save(school: School) = (this returning this.map(_.id)).insertOrUpdate(school)
    def list() = compiledList.result
  }

  case class Course(id: Int = 0, schoolId: Int, name: String, website: Option[String] = None)
  class Courses(tag: Tag) extends Table[Course](tag, "courses") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def schoolId = column[Int]("school_id")
    def name = column[String]("name")
    def website = column[Option[String]]("website")
    def * = (id, schoolId, name, website) <> (Course.tupled, Course.unapply)
    def schoolFk = foreignKey("school_fk", schoolId, TableQuery[Schools])(_.id)
  }
  object courses extends TableQuery(new Courses(_)) {
    val compiledListBySchool = Compiled { schoolId: Rep[Int] => filter(_.schoolId === schoolId).sortBy(_.name.asc) }
    def save(course: Course) = (this returning this.map(_.id)).insertOrUpdate(course)
    def list(schoolId: Int) = compiledListBySchool(schoolId).result
  }

  case class Student(id: Int = 0, name: String, email: String, born: LocalDate)
  class Students(tag: Tag) extends Table[Student](tag, "students") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email", O.Unique)
    def born = column[LocalDate]("born")
    def * = (id, name, email, born) <> (Student.tupled, Student.unapply)
  }
  object students extends TableQuery(new Students(_)) {
    val compiledList = Compiled { sortBy(_.name.asc) }
    def save(student: Student) = (this returning this.map(_.id)).insertOrUpdate(student)
    def list() = compiledList.result
  }

  case class Grade(id: Int = 0, studentId: Int, grade: Int, started: LocalDate = LocalDate.now, completed: LocalDate = LocalDate.now.plusMonths(6))
  class Grades(tag: Tag) extends Table[Grade](tag, "grades") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def studentId = column[Int]("student_id")
    def grade = column[Int]("grade")
    def started = column[LocalDate]("started")
    def completed = column[LocalDate]("completed")
    def * = (id, studentId, grade, started, completed) <> (Grade.tupled, Grade.unapply)
    def studentFk = foreignKey("student_fk", studentId, TableQuery[Students])(_.id)
  }
  object grades extends TableQuery(new Grades(_)) {
    val compiledListByStudent = Compiled { studentId: Rep[Int] => filter(_.studentId === studentId).sortBy(_.grade.asc) }
    def save(grade: Grade) = (this returning this.map(_.id)).insertOrUpdate(grade)
    def list(studentId: Int) = compiledListByStudent(studentId).result
  }

  case class Assignment(id: Int = 0, gradeId: Int, courseId: Int, task: String, assigned: LocalDateTime = LocalDateTime.now, completed: Option[LocalDateTime] = None, score: Double = 0.0)
  class Assignments(tag: Tag) extends Table[Assignment](tag, "assignments") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def gradeId = column[Int]("grade_id")
    def courseId = column[Int]("course_id")
    def task = column[String]("task")
    def assigned = column[LocalDateTime]("assigned")
    def completed = column[Option[LocalDateTime]]("completed")
    def score = column[Double]("score")
    def * = (id, gradeId, courseId, task, assigned, completed, score) <> (Assignment.tupled, Assignment.unapply)
    def gradeFk = foreignKey("grade_assignment_fk", gradeId, TableQuery[Grades])(_.id)
    def courseFK = foreignKey("course_assignment_fk", courseId, TableQuery[Courses])(_.id)
  }
  object assignments extends TableQuery(new Assignments(_)) {
    val compiledListByStudentGradeCourse = Compiled { (gradeId: Rep[Int], courseId: Rep[Int]) => filter(a => a.gradeId === gradeId && a.courseId === courseId).sortBy(_.assigned.asc) }
    val compiledCalculateScore = Compiled { (gradeId: Rep[Int], courseId: Rep[Int]) => filter(a => a.gradeId === gradeId && a.courseId === courseId).map(_.score).sum }
    def save(assignment: Assignment) = (this returning this.map(_.id)).insertOrUpdate(assignment)
    def list(gradeId: Int, courseId: Int) = compiledListByStudentGradeCourse( (gradeId, courseId) ).result
    def calculateScore(gradeId: Int, courseId: Int) = compiledCalculateScore( (gradeId, courseId) ).result
  }
}